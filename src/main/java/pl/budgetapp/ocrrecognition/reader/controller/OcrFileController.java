package pl.budgetapp.ocrrecognition.reader.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.budgetapp.ocrrecognition.reader.dto.Receipt;
import pl.budgetapp.ocrrecognition.reader.utils.FileUploadUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class OcrFileController {

    @PostMapping("/uploadFile")
    public ResponseEntity<Receipt> recognizeReceipt(@RequestParam("file") MultipartFile multipartFile){

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        log.info("File size: " + multipartFile.getSize());

        try {
            fileName = FileUploadUtil.saveFile(fileName, multipartFile);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        File image = new File("src/main/resources/uploaded-files/" + fileName);
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("pol");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        String result = null;
        try {
            result = tesseract.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        log.info(result);

        Receipt receipt = new Receipt("Test", LocalDate.now(),99.0);
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    @PostMapping("/uploadFiles")
    public ResponseEntity<Receipt> recognizeReceipts(@RequestParam("file") MultipartFile[] file){
        return null;
    }
}
