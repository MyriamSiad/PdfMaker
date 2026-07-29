package fr.pdfmaker.backend.fonctionnel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TxtToPdfFonctionnelTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void testFlotComplet_ConversionTxtVersPdf_Succes() throws Exception {

        MockMultipartFile fichierTxt = new MockMultipartFile(
                "fichier",
                "mon_rapport.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Ligne 1 : Contenu de test.\nLigne 2 : Suite du rapport.".getBytes()
        );


        mockMvc.perform(multipart("/api/rest/pdf/conversion/txt-to-pdf")
                        .file(fichierTxt)
                        .param("charset", "UTF-8")
                        .param("nomFichierSortie", "rapport_final"))


                .andExpect(status().isOk()) // Statut HTTP 200 OK
                .andExpect((ResultMatcher) jsonPath("$.nomFichierSortie").value("rapport_final.pdf"))
                .andExpect((ResultMatcher) jsonPath("$.fichierPdf").exists());
    }
}
