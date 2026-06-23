package io.github.yonecircle.watchtransform;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        // 各テストの前にインスタンスを作成
        calculator = new Calculator();
    }

    @Test
    @DisplayName("2 + 3 = 5 になること")
    void testAdd() {
        int result = calculator.add(2, 3);
        assertEquals(5, result, "2 + 3 は 5 になるべき");
    }

    @Test
    @DisplayName("5 - 3 = 2 になること")
    void testSubtract() {
        int result = calculator.subtract(5, 3);
        assertEquals(2, result, "5 - 3 は 2 になるべき");
    }

    @Test
    @DisplayName("4 × 3 = 12 になること")
    void testMultiply() {
        int result = calculator.multiply(4, 3);
        assertEquals(12, result, "4 × 3 は 12 になるべき");
    }

    @Test
    @DisplayName("10 ÷ 2 = 5 になること")
    void testDivide() {
        int result = calculator.divide(10, 2);
        assertEquals(5, result, "10 ÷ 2 は 5 になるべき");
    }

    @Test
    @DisplayName("0で割ると例外が発生すること")
    void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10, 0);
        });
    }
}
