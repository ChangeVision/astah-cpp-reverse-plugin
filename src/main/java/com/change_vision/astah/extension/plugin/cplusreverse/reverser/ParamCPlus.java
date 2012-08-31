package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.util.Set;

import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.model.IElement;

public class ParamCPlus extends Param {
    @Override
    void dealKeyword(IElement param, Set<String> keywords) throws InvalidEditingException {
        if (keywords.contains(AND)) {
            param.setTypeModifier(AND);
        } else if (keywords.contains(STAR + STAR)) {
            param.setTypeModifier(STAR + STAR);
        } else if (keywords.contains(STAR)) {
            param.setTypeModifier(STAR);
        }
    }
}