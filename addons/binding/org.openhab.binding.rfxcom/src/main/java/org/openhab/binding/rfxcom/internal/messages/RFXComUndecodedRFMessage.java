/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.smarthome.core.library.items.StringItem;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.Type;
import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComException;

/**
 * RFXCOM data class for undecoded messages.
 *
 * @author James Hewitt-Thomas
 */
public class RFXComUndecodedRFMessage extends RFXComBaseMessage {

    public enum SubType {
        AC(0x00),
        ARC(0x01),
        ATI(0x02),
        HIDEKI_UPM(0x03),
        LACROSSE_VIKING(0x04),
        AD(0x05),
        MERTIK(0x06),
        OREGON1(0x07),
        OREGON2(0x08),
        OREGON3(0x09),
        PROGUARD(0x0a),
        VISONIC(0x0b),
        NEC(0x0c),
        FS20(0x0d),
        RESERVED(0x0e),
        BLINDS(0x0f),
        RUBICSON(0x10),
        AE(0x11),
        FINE_OFFSET(0x12),
        RGB(0x13),
        RTS(0x14),
        SELECT_PLUS(0x15),
        HOME_CONFORT(0x16),

        UNKNOWN(255);

        private final int subType;

        SubType(int subType) {
            this.subType = subType;
        }

        SubType(byte subType) {
            this.subType = subType;
        }

        public byte toByte() {
            return (byte) subType;
        }
    }

    private final static List<RFXComValueSelector> supportedInputValueSelectors = Arrays
            .asList(RFXComValueSelector.RAW_MESSAGE, RFXComValueSelector.RAW_PAYLOAD);

    private final static List<RFXComValueSelector> supportedOutputValueSelectors = Arrays.asList();

    public SubType subType = SubType.UNKNOWN;
    public byte[] rawPayload = new byte[0];

    public RFXComUndecodedRFMessage() {
        packetType = PacketType.UNDECODED_RF_MESSAGE;
    }

    public RFXComUndecodedRFMessage(byte[] message) {
        encodeMessage(message);
    }

    @Override
    public String toString() {

        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + getDeviceId();
        str += "\n - Message = " + DatatypeConverter.printHexBinary(rawMessage);

        return str;
    }

    @Override
    public void encodeMessage(byte[] message) {

        super.encodeMessage(message);

        try {
            subType = SubType.values()[super.subType];
        } catch (Exception e) {
            subType = SubType.UNKNOWN;
        }

        rawPayload = Arrays.copyOfRange(rawMessage, 4, rawMessage.length);
    }

    @Override
    public byte[] decodeMessage() {

        final int rawPayloadLen = Math.min(rawPayload.length, 33);
        byte[] data = new byte[4 + rawPayloadLen];

        data[0] = (byte) (data.length - 1);
        data[1] = RFXComBaseMessage.PacketType.UNDECODED_RF_MESSAGE.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;

        for (int i = 0; i < rawPayloadLen; i++) {
            data[i + 4] = rawPayload[i];
        }
        return data;
    }

    @Override
    public String getDeviceId() {
        return "UNDECODED";
    }

    @Override
    public State convertToState(RFXComValueSelector valueSelector) throws RFXComException {

        if (valueSelector.getItemClass() == StringItem.class) {
            if (valueSelector == RFXComValueSelector.RAW_MESSAGE) {
                return new StringType(DatatypeConverter.printHexBinary(rawMessage));
            } else if (valueSelector == RFXComValueSelector.RAW_PAYLOAD) {
                return new StringType(DatatypeConverter.printHexBinary(rawPayload));
            }
        }

        throw new RFXComException("Can't convert " + valueSelector + " to " + valueSelector.getItemClass());
    }

    @Override
    public void setSubType(Object subType) throws RFXComException {
        throw new RFXComException("Not supported");
    }

    @Override
    public void setDeviceId(String deviceId) throws RFXComException {
        throw new RFXComException("Not supported");
    }

    @Override
    public void convertFromState(RFXComValueSelector valueSelector, Type type) throws RFXComException {
        throw new RFXComException("Not supported");
    }

    @Override
    public Object convertSubType(String subType) throws RFXComException {

        for (SubType s : SubType.values()) {
            if (s.toString().equals(subType)) {
                return s;
            }
        }

        // try to find sub type by number
        try {
            return SubType.values()[Integer.parseInt(subType)];
        } catch (Exception e) {
            throw new RFXComException("Unknown sub type " + subType);
        }
    }

    @Override
    public List<RFXComValueSelector> getSupportedInputValueSelectors() throws RFXComException {
        return supportedInputValueSelectors;
    }

    @Override
    public List<RFXComValueSelector> getSupportedOutputValueSelectors() throws RFXComException {
        return supportedOutputValueSelectors;
    }

}
