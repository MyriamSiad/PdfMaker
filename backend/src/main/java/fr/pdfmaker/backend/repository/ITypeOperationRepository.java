package fr.pdfmaker.backend.repository;



import fr.pdfmaker.backend.enums.LibelleOperationEnum;
import fr.pdfmaker.backend.model.entity.TypeOperation;

import java.lang.reflect.Type;
import java.util.List;

public interface ITypeOperationRepository {
    List<TypeOperation>getAllTypeOperation();
    TypeOperation findByIdTypeOperation(Long idTypeOperation);
    List<TypeOperation>findByLibelleOperation(LibelleOperationEnum libelleOperation );

}
