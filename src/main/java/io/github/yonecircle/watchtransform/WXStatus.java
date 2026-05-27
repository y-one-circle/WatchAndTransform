package io.github.yonecircle.watchtransform;

public enum WXStatus {
    WAITING,            //待機中
    WATCHING,           //endDir監視中
    PROCESSING,         //ファイル編集実行中
    COMPLETED,          //ファイル編集完了
    VALIDATION_ERROR,   //ユーザが対処できるエラー
    SYSTEM_ERROR;       //ユーザが対処できないエラー
}