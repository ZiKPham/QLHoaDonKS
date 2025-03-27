package avgAmountItemKS.UseCase;

import java.util.List;

import avgAmountItemKS.Entity.AverageAmount;
import avgAmountItemKS.Entity.Invoice;

public class AverageAmountUseCase implements AverageAmountInputBoundary {
    private AverageAmountOutputBoundary averageAmountOutputBoundary;
    private AverageAmountDatabaseBoundary averageAmountDatabaseBoundary;
    private AverageAmount averageAmount;
    private DataMessageAvg dataMessage;

    public AverageAmountUseCase(AverageAmountOutputBoundary averageAmountOutputBoundary,
            AverageAmountDatabaseBoundary averageAmountDatabaseBoundary, AverageAmount averageAmount,
            ResponseDataAvg responseData) {
        this.averageAmountOutputBoundary = averageAmountOutputBoundary;
        this.averageAmountDatabaseBoundary = averageAmountDatabaseBoundary;
        this.averageAmount = averageAmount;
        this.dataMessage = (DataMessageAvg) responseData;
    }

    @Override
    public void execute(AverageAmountInputDTO averageAmountInputDTO) {

        try {
            int month = Integer.parseInt(averageAmountInputDTO.getThangHD());
            if (month < 1 || month > 12) {
                dataMessage.setErrorMessage("Tháng không hợp lệ, phải từ 1 đến 12.");
                dataMessage.setMessage(null);
                averageAmountOutputBoundary.exportMessage(dataMessage);
                return;
            }
        } catch (NumberFormatException e) {
            dataMessage.setErrorMessage("Vui lòng nhập tháng!");
            dataMessage.setMessage(null);
            averageAmountOutputBoundary.exportMessage(dataMessage);
            return;
        }

        List<Invoice> invoices = averageAmountDatabaseBoundary.getAverageAmountInvoices(averageAmountInputDTO);
        List<AverageAmountOutPutDTO> listOut = averageAmount.calculate(invoices);
        averageAmountOutputBoundary.present(listOut);
    }
}
