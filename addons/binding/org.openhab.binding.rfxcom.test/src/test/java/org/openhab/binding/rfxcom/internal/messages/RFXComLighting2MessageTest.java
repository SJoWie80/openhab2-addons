/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComException;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComNotImpException;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertEquals;

public class RFXComLighting2MessageTest {

    @Test
    public void testSomeMessages() throws RFXComException, RFXComNotImpException {
        String hexMessage = "0B11000600109B520B000080";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComLighting2Message msg = (RFXComLighting2Message) RFXComMessageFactory.createMessage(message);
        assertEquals("SubType", RFXComLighting2Message.SubType.AC, msg.subType);
        assertEquals("Seq Number", 6, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", "1088338.11", msg.getDeviceId());
        assertEquals("Command", RFXComLighting2Message.Commands.OFF, msg.command);
        assertEquals("Signal Level", (byte) 8, msg.signalLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}