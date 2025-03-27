package deleteInvoiceDisplayKS.UI;

import deleteInvoiceKS.UseCase.DeleteInvoiceOutputBoundary;
import deleteInvoiceKS.UseCase.ResponseDataDelete;
import deleteInvoiceKS.UseCase.DataMessageDelete;
import java.util.List;
import java.util.Observable;

import deleteInvoiceDisplayKS.UseCase.DeleteDisplayOutputBoundary;
import deleteInvoiceDisplayKS.UseCase.DeleteInvoiceOutputDTO;

public class DeleteDisplayPresenter extends Observable
        implements DeleteInvoiceOutputBoundary, DeleteDisplayOutputBoundary {
    private DataMessageDelete dataMessage;
    private DeleteViewModel deleteIVModel;

    public DeleteDisplayPresenter(DeleteViewModel deleteIVModel) {
        this.deleteIVModel = deleteIVModel;
    }

    public String getErrorMessage() {
        return this.dataMessage.getErrorMessage();
    }

    public String getMessage() {
        return this.dataMessage.getMessage();
    }

    public String getConfirmMessage() {
        return this.dataMessage.getConfirmMessage();
    }

    @Override
    public void exportMessage(ResponseDataDelete responseData) {
        dataMessage = (DataMessageDelete) responseData;
        if (dataMessage.isConfirmationRequired()) {
            deleteIVModel.setConfirmMesage(dataMessage.getConfirmMessage());
            setChanged();
            notifyObservers("CONFIRM_DELETE");
            dataMessage.setConfirmationRequired(false);
        } else {
            if (dataMessage.getErrorMessage() != null) {
                deleteIVModel.setErrorMessage(dataMessage.getErrorMessage());
                setChanged();
                notifyObservers();
            } else if (dataMessage.getMessage() != null) {
                deleteIVModel.setMessage(dataMessage.getMessage());
                setChanged();
                notifyObservers("DELETE");
            }
        }
    }

    @Override
    public void presentInvoiceIds(List<DeleteInvoiceOutputDTO> listOutput) {
        String[] invoiceIds = listOutput.get(0).getListMaHD();
        deleteIVModel.setInvoiceIds(invoiceIds);
        setChanged();
        notifyObservers("UPDATE_INVOICE_IDS");
    }

}
