package connect.as2.as2middleware.service.impl;

import connect.as2.as2middleware.config.APIException;
import connect.as2.as2middleware.service.EntryService;
import connect.as2.as2middleware.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Odinaka Onah on 01 Jun, 2021.
 */
@Service
@Slf4j
public class EntryServiceImpl implements EntryService {

    private final FileService fileService;

    public EntryServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    public Response sendToAnyPartner(MultipartFile file) {

        if (file == null)
            throw new APIException(new Response("16", "File to be uploaded cannot be null", null));
        var originalFilename = file.getOriginalFilename();
        var split = originalFilename.split("-");
        if (split.length != 3)
            throw new APIException(new Response("16", "Invalid file name. It must be in this format: MyCompany-YourCompany-TheEdiFileNameToBeSent.<file extension>", null));
        fileService.save("Any",file);
        return new Response(200, "00", "File Dropped successfully", null);
    }
    public Response sendToOnePartner(String partnerName,MultipartFile file) {

        if (file == null)
            throw new APIException(new Response("16", "File to be uploaded cannot be null", null));
        fileService.save(partnerName,file);
        return new Response(200, "00", "File Dropped successfully", null);
    }
}
