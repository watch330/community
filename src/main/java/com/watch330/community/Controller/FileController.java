package com.watch330.community.Controller;

import com.watch330.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO uploadFile(){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setMessage("ceshi");
        fileDTO.setUrl("\\images\\logos\\editormd-logo-32x32.png");
        return fileDTO;
    }
}
