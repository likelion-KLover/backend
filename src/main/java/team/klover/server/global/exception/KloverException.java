package team.klover.server.global.exception;

import lombok.Getter;
import team.klover.server.global.i18n.service.LocaleMessageService;

@Getter
public class KloverException extends RuntimeException {

  private ReturnCode returnCode;
  private String returnMessage;

  public KloverException(ReturnCode returnCode) {
    super(returnCode.getReturnMessage());
    this.returnCode = returnCode;
    this.returnMessage = LocaleMessageService.getMessage(returnCode.getReturnMessage());
  }
}
