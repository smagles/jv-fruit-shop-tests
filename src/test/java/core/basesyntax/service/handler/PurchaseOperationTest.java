package core.basesyntax.service.handler;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.storage.Storage;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PurchaseOperationTest {
    private static OperationHandler operationHandler;
    private Storage storage;

    @BeforeAll
    static void beforeAll() {
        operationHandler = new PurchaseOperation();
    }

    @BeforeEach
    public void setUp() {
        storage = new Storage();

        Map<String, Integer> initialFruits = new HashMap<>();
        initialFruits.put("apple", 10);
        initialFruits.put("banana", 5);
        storage.setFruits(initialFruits);
    }

    @Test
    public void transaction_existingFruit_decreasesQuantity() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "apple", 3);

        operationHandler.transaction(fruitTransaction, storage);

        int expectedQuantity = 7;
        int actualQuantity = storage.getFruits().get("apple");

        Assertions.assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    public void transaction_exactQuantity_zerosQuantity() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "banana", 5);

        operationHandler.transaction(fruitTransaction, storage);

        int expectedQuantity = 0;
        int actualQuantity = storage.getFruits().get("banana");

        Assertions.assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    public void transaction_insufficientQuantity_notOk() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "banana", 10);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                operationHandler.transaction(fruitTransaction, storage));
    }

    @Test
    public void transaction_fruitNotInStorage_notOk() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "orange", 1);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                operationHandler.transaction(fruitTransaction, storage));
    }

    @Test
    public void transaction_zeroQuantity_noChange() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "banana", 0);

        operationHandler.transaction(fruitTransaction, storage);

        int expectedQuantity = 5;
        int actualQuantity = storage.getFruits().get("banana");

        Assertions.assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    public void transaction_emptyFruitName_notOk() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "", 5);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            operationHandler.transaction(fruitTransaction, storage);
        });
    }

    @Test
    public void transaction_nullFruitName_notOk() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, null, 5);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            operationHandler.transaction(fruitTransaction, storage);
        });
    }

    @Test
    public void transaction_nullFruitTransaction_notOk() {
        FruitTransaction fruitTransaction = null;

        Assertions.assertThrows(NullPointerException.class, () -> {
            operationHandler.transaction(fruitTransaction, storage);
        });
    }
}
