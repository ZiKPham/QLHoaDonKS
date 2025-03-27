package com.addinvoice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import avgAmountDisplayKS.UI.AvgAmountDisplayPresenter;
import avgAmountDisplayKS.UI.AvgAmountDisplayViewModel;
import avgAmountItemKS.Database.AverageAmountDAOMySQL;
import avgAmountItemKS.Database.ConnectionDBAmount;
import avgAmountItemKS.Entity.AverageAmount;
import avgAmountItemKS.Entity.AverageAmountImpl;
import avgAmountItemKS.Entity.Invoice;
import avgAmountItemKS.UseCase.AverageAmountDatabaseBoundary;
import avgAmountItemKS.UseCase.AverageAmountInputDTO;
import avgAmountItemKS.UseCase.AverageAmountOutPutDTO;
import avgAmountItemKS.UseCase.AverageAmountOutputBoundary;
import avgAmountItemKS.UseCase.AverageAmountUseCase;
import avgAmountItemKS.UseCase.DataMessageAvg;

public class AverageAmountTest {

    private AverageAmountDatabaseBoundary averageAmountDatabaseBoundary;
    private AverageAmountOutputBoundary averageAmountOutputBoundary;
    private AverageAmount averageAmountBusinessRules;
    private AverageAmountUseCase averageAmountUseCase;
    private AvgAmountDisplayViewModel viewModel;
    private DataMessageAvg dataMessage;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        viewModel = new AvgAmountDisplayViewModel();
        dataMessage = new DataMessageAvg();
        ConnectionDBAmount connectionDBAmount = ConnectionDBAmount.getInstance();
        averageAmountDatabaseBoundary = new AverageAmountDAOMySQL(connectionDBAmount);
        averageAmountOutputBoundary = new AvgAmountDisplayPresenter(viewModel);
        averageAmountBusinessRules = new AverageAmountImpl();
        averageAmountUseCase = new AverageAmountUseCase(
                averageAmountOutputBoundary,
                averageAmountDatabaseBoundary,
                averageAmountBusinessRules,
                dataMessage);
    }

    @Test
    public void testExecute() {
        AverageAmountInputDTO averageAmountInputDTO = new AverageAmountInputDTO("10");
        List<Invoice> summaryList = averageAmountDatabaseBoundary.getAverageAmountInvoices(averageAmountInputDTO);

        List<AverageAmountOutPutDTO> listCalc = averageAmountBusinessRules.calculate(summaryList);

        AverageAmountOutPutDTO summary = listCalc.stream().findFirst().orElse(null);
        assertNotNull(summary);
        assertTrue(summary.getTongSoTien() >= 0);
        assertTrue(summary.getTrungBinhThanhTien() >= 0);

        assertEquals(325000.0, summary.getTongSoTien(), 0.01);
        assertEquals(21666.67, summary.getTrungBinhThanhTien(), 0.01);
    }

    /*
     * Tháng không hợp lệ
     */
    @Test
    public void testExecuteErrorMonth() {
        AverageAmountInputDTO averageAmountInputDTO = new AverageAmountInputDTO("19");
        averageAmountUseCase.execute(averageAmountInputDTO);
        assertEquals("Tháng không hợp lệ, phải từ 1 đến 12.", ((DataMessageAvg) dataMessage).getErrorMessage());

    }

    // Chưa nhập gì hết
    @Test
    public void testExecuteErrorNull() {
        AverageAmountInputDTO averageAmountInputDTO = new AverageAmountInputDTO("");
        averageAmountUseCase.execute(averageAmountInputDTO);
        assertEquals("Vui lòng nhập tháng!", ((DataMessageAvg) dataMessage).getErrorMessage());
    }
}
