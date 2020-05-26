package com.watch330.community.advice;

import com.watch330.community.dto.ResultDTO;
import com.watch330.community.exception.CustomizeException;
import com.watch330.community.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理controller层级的异常.
 */
@ResponseBody
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request, Throwable e, Model model) {
        HttpStatus status = getStatus(request);
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO = null;
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                resultDTO = ResultDTO.errorOf(ErrorCode.SYS_ERROR);
            }
            return resultDTO;
        } else {
            if (e instanceof CustomizeException) {
                model.addAttribute("errorCode", status.value());
                model.addAttribute("exMessage", e.getMessage());
            } else {
                model.addAttribute("errorCode", status.value());
                model.addAttribute("exMessage", "服务出了点故障...");
            }

            return new ModelAndView("error");
        }

    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
