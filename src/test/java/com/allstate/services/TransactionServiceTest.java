package com.allstate.services;

import com.allstate.entities.Transaction;
import com.allstate.entities.User;
import com.allstate.enums.Action;
import com.allstate.models.Stock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(value = {"/sql/seed.sql"})
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;

    @MockBean
    private QuoteService quoteService;

    @Before
    public void setUp() throws Exception {
        // stub
        when(this.quoteService.quote("AAPL")).thenReturn(new Stock("Apple Inc", "AAPL", 100.0));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldPurchaseStock() throws Exception {
        Transaction transaction = this.transactionService.buy(2, "AAPL", 5);
        assertEquals(9, transaction.getId());
        assertEquals(2, transaction.getUser().getId());
        assertEquals(Action.BUY, transaction.getAction());
        assertEquals(5, transaction.getQuantity());
        assertEquals("AAPL", transaction.getSymbol());
        assertEquals(500.0, transaction.getAmount(), 0.1);
    }

    @Test(expected = com.allstate.exceptions.TransactionException.class)
    public void shouldNotPurchaseStockInsufficientFunds() throws Exception {
        Transaction transaction = this.transactionService.buy(1, "AAPL", 5);
    }
}