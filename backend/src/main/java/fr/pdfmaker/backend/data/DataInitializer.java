package fr.pdfmaker.backend.data;

import fr.pdfmaker.backend.enums.LibelleOperationEnum;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/*@Component
/*public class DataInitializer implements ApplicationRunner {

    private final ITypeOperationRepository typeOperationRepository;

    public DataInitializer(ITypeOperationRepository typeOperationRepository) {
        this.typeOperationRepository = typeOperationRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (typeOperationRepository.count() == 0) {  // N'insère qu'une seule fois
            Arrays.stream(LibelleOperationEnum.values())
                    .forEach(libelle -> {
                        TypeOperation type = new TypeOperation();
                        type.setLibelleOperation(String.valueOf(libelle));
                        typeOperationRepository.save(type);
                    });
        }
    }
}
*/