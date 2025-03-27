package avgAmountDisplayKS.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import avgAmountItemKS.UseCase.AverageAmountOutPutDTO;
import avgAmountItemKS.UseCase.AverageAmountOutputBoundary;
import avgAmountItemKS.UseCase.DataMessageAvg;
import avgAmountItemKS.UseCase.ResponseDataAvg;
import avgAmountDisplayKS.UseCase.AvgAmountDisplayOutputDTO;
import avgAmountDisplayKS.UseCase.AvgAmountDisplayOutputBoundary;

public class AvgAmountDisplayPresenter extends Observable implements AverageAmountOutputBoundary, AvgAmountDisplayOutputBoundary {
    private AvgAmountDisplayViewModel viewModel;
    private DataMessageAvg dataMessage;

    public AvgAmountDisplayPresenter(AvgAmountDisplayViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(List<AverageAmountOutPutDTO> averageAmoutOutPutDTOs) {
        List<String[]> summaryList = new ArrayList<>();

        for (AverageAmountOutPutDTO dto : averageAmoutOutPutDTOs) {
            String[] data = {
                    String.valueOf(dto.getTongSoTien()),
                    String.valueOf(dto.getTrungBinhThanhTien())
            };
            summaryList.add(data);
        }

        viewModel.setAverageAmountList(summaryList);
        setChanged();
        notifyObservers();
    }

    @Override
    public void exportMessage(ResponseDataAvg responseData) {
        dataMessage = (DataMessageAvg) responseData;

        if (dataMessage.getErrorMessage() != null) {
            viewModel.setErrorMessage(dataMessage.getErrorMessage());
        } else if (dataMessage.getMessage() != null) {
            viewModel.setMessage(dataMessage.getMessage());
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public void presentMonthList(AvgAmountDisplayOutputDTO monthListDTO) {
        viewModel.setMonthList(monthListDTO.getMonthList());
        setChanged();
        notifyObservers("UPDATE_MONTH_LIST");
    }
}
