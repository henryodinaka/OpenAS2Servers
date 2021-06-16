package connect.as2.as2middleware.controller;

import connect.as2.as2middleware.service.EntryService;
import connect.as2.as2middleware.utils.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/file")
public class FileController {
    private final EntryService entryService;

    public FileController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping(value = "/upload/generic",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> uploadToAny(@RequestParam(value = "file") MultipartFile multipartFile) {


            Response response = this.entryService.sendToAnyPartner(multipartFile);
            return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> uploadTo(@RequestParam String partnerName, @RequestParam(value = "file") MultipartFile multipartFile) {


            Response response = this.entryService.sendToOnePartner(partnerName,multipartFile);
            return ResponseEntity.status(response.getHttpCode()).body(response);
    }

}