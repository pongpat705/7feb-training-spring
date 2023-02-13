package th.co.prior.training.spring.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileAndAttributeModel {
    private String name;
    private MultipartFile file;
}
