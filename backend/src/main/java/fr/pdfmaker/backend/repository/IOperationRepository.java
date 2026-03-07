package fr.pdfmaker.backend.repository;

import fr.pdfmaker.backend.model.entity.Operation;

import java.util.List;

public interface IOperationRepository {

    Operation findByIdOperation(Long idOperation);

    List<Operation> getOperationStatusFailed();

    List<Operation>getOperationStatusSuccess();
}
