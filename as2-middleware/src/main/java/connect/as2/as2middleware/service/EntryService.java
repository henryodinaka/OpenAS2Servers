package connect.as2.as2middleware.service;

import connect.as2.as2middleware.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Odinaka Onah on 01 Jun, 2021.
 */
public interface EntryService {
    Response sendToAnyPartner(MultipartFile multipartFile);
    Response sendToOnePartner(String partnerName,MultipartFile multipartFile);
}
