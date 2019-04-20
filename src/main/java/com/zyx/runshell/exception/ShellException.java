package com.zyx.runshell.exception;

import com.dexcoder.commons.enums.IEnum;
import com.dexcoder.commons.exceptions.DexcoderException;

public class ShellException extends DexcoderException {
    public ShellException(IEnum e) {
        super(e);
    }

    public ShellException(String message) {
        super(message);
    }

    public ShellException(String code, String message) {
        super(code, message);
    }
}
