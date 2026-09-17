# WatchAndTransform

## 概要
ファイルを監視し、テキストファイルを編集した上で、処理が完了したことを示すWXendファイルを出力するWebツールです。Web UIから監視ディレクトリと変換ルールを設定できます。WXendファイルは、変換処理が完了したことを後続処理へ知らせるための完了通知ファイルとして扱っています。
実行後は画面が遷移し、「監視中」→「変換処理実行中」→「WX完了」という処理の進捗をブラウザ上でリアルタイムに確認できます。

## 作った背景
現職のQA業務で外部ソフトの出力に対する、こちらのソフトの挙動の手動チェック作業が繰り返し発生しており、
「検知→処理→通知」を自動化したいと考えたことが出発点です。
業務で実際に発生する課題を題材にすることで、
設計判断に現実的な根拠を持たせることを意識しました。

## 処理フロー

1. Web UI（index画面）にアクセスすると、保存済みのプロパティファイルから前回の設定値を読み込んで入力欄に反映する
2. Web UIから監視ディレクトリ、テキストファイルディレクトリ、一時ファイルディレクトリ、リターンコード、SuffixModeを設定する
3. 「実行」ボタンを押すと非同期でWX処理が開始され、画面はexecutionStatus画面へ遷移する
4. executionStatus画面はステータスAPIを1秒間隔でポーリングし、現在の処理状況を表示し続ける
5. サーバ側では指定ディレクトリの監視を開始する（ステータス：監視中）
6. endファイルの作成を検知する
7. 検知したendファイルに対応するテキストファイルを一時ファイルディレクトリにコピーする（ステータス：変換処理実行中）
8. テキストファイルに変換ルール（データ件数の半減、Suffixの付加/除去など）を適用する
9. 変換後のテキストファイルを、SuffixModeに応じたファイル名で元のディレクトリへ移動する
10. 処理完了を示すWXendファイルへリターンコードを書き込んで生成し、監視ディレクトリへ移動する
11. 全処理が正常終了するとステータスが完了になり、ブラウザ側のポーリングが「WX完了」を表示してポーリングを停止する
12. 途中でユーザ入力の不備やI/Oエラーが発生した場合はステータスがエラー系に変わり、画面にエラーメッセージが表示される

## 技術選定の理由
|技術 | 選定理由 |
|------|---------|
|  Java | 業務での実績が多くあり、コンパイルチェックによる型安全性と例外処理の厳格さで堅牢なツールにするためです。|
|  Spring Boot | 業務でも広く使われるスタックで実務に近い開発経験を積むためです。DIやMVCの仕組みを意図的に活用することでロジックの分離を実現しました。|
|  Thymeleaf | Spring Bootと親和性の高いテンプレートエンジンで、画面（index/executionStatus）の切り替えをController側のルーティングだけで完結できるためです。|

## 構成
- Controller（WXController）：画面遷移用のエンドポイント（index, executionStatus）と、API用のエンドポイント（/api/wx/execute, /api/wx/status, /api/wx/config）を提供する
- ServiceProcess：監視から変換処理までの一連の流れを@Asyncで非同期に実行し、各段階でStatusHolderへ状態を反映する
- Watcher：WatchServiceを使ってディレクトリを監視し、.endファイルの作成を検知する
- Service群（TextCopy / TextEditor / TextMove / XENDGenerator / XENDMove）：ファイルのコピー・編集・移動・XENDファイル生成という単一責務ごとにクラスを分割している
- StatusHolder / WXStatus：現在の処理状態（WAITING / WATCHING / PROCESSING / COMPLETED / VALIDATION_ERROR / SYSTEM_ERROR）とエラーメッセージを一元管理する
- ConfigService：入力されたパス等をwx.propertiesへ保存・再読込する
- exception（ValidationException / SystemException / GlobalExceptionHandler）：ユーザ起因のエラーとシステム起因のエラーを型で区別し、@ControllerAdviceで統一的にレスポンスへ変換する

## 設計上の判断と理由
- **コンストラクタインジェクションを用いることでフィールドをfinalにできるようにしました。
- **サービス層を設けることでビジネスロジックを分離**：サービスクラスでパスの生成を一括で担うことで、パス生成のミスを発見しやすくしました。QAエンジニアの視点からテストのしやすさに配慮しました。
- **サービスクラス、監視クラスの入出力をコントローラーに集約**：カプセル化を狙うことでそれぞれの機能でテストを実施しやすく、早い段階で機能の問題点を洗い出すことができました。
- **内部処理の状態をenumとして持つことで、取りうる値を型レベルで制限しました**：WXStatus enumで状態を6種類に限定し、StatusHolderで一元管理することで、ステータスAPIとフロント側の表示ロジックが対応しやすくなりました。
- **例外を2種類に分けてユーザへの通知を改善**：ValidationException（ユーザが対処できる入力ミス）とSystemException（ユーザが対処できないI/O等のエラー）を分離し、GlobalExceptionHandlerとStatusHolder経由でexecutionStatus画面にエラーメッセージとして表示できるようにしました。
- **非同期処理の進捗をポーリングで可視化**：@Asyncで別スレッド実行しつつ、フロント側は1秒間隔でステータスAPIを叩くことで、処理完了を待つ間もユーザが今どの段階かを把握できるようにしました。

## 工夫した点
- キーバリューのプロパティファイル情報を保存することで、ユーザーが毎回ディレクトリと変換ルールを入力しなくて済むようにしました。
- ステータスをenumで細分化してAPIから返すことで、画面側は状態名と日本語表示のマッピングだけを持てばよく、表示ロジックとサーバ側のロジックを疎結合にしました。

## 現状の課題・今後やること
- [x] テストコードを作成します → Calculator/TextCopy/TextEditor/TextMove/XENDGenerator/XENDMove/ServiceProcessの各テストクラスと、Spring Bootの起動確認テストを作成済み
- [ ] ファイル検知、ファイル作成などのイベントをログで確認できるようにします（現状はexecute()内にSystem.out.printlnによる確認用出力があるのみで、正式なログ基盤は未導入）
- [ ] コントローラーに処理が集まり過ぎている点を解消します（WXController.execute()内に残っている入力バリデーションとString→Pathの変換処理を、専用クラスへ切り出す予定）
- [ ] 本体コードから未使用になったCalculatorクラス（テストからのみ参照）を整理します

## v1（ReadMe.txt）からの主な変更点
- 非同期実行＋ステータスポーリングによる進捗表示（監視中／変換処理実行中／WX完了）の仕組みを追加
- 「例外処理が不十分」だった課題を解消し、ValidationException / SystemException / GlobalExceptionHandlerによる画面通知を実装済みに更新
- 「テストコードを作成します」だったTODOを実施済みに更新（8テストクラス）
- 構成セクションを、実際のクラス名（WXController, ServiceProcess, Watcher, StatusHolder, ConfigService等）で具体化
- 新たな課題として、コントローラーに残る入力バリデーション/Path変換ロジックの切り出しと、未使用のCalculatorクラスの整理を追記

## 動作確認方法
```bash
git clone https://github.com/y-one-circle/WatchAndTransform
cd WatchAndTransform
./mvnw spring-boot:run
# localhost:8080/index でUI確認
```
