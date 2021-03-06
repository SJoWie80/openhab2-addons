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

public class RFXComLighting6MessageTest {

    @Test
    public void testSomeMessages() throws RFXComException, RFXComNotImpException {
        String hexMessage = "0B150005D950450101011D80";
        byte[] message = DatatypeConverter.parseHexBinary(hexMessage);
        RFXComLighting6Message msg = (RFXComLighting6Message) RFXComMessageFactory.createMessage(message);
        assertEquals("SubType", RFXComLighting6Message.SubType.BLYSS, msg.subType);
        assertEquals("Seq Number", 5, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", "55632.E.1", msg.getDeviceId());
        assertEquals("Command", RFXComLighting6Message.Commands.OFF, msg.command);
        assertEquals("Signal Level", (byte) 8, msg.signalLevel);

        byte[] decoded = msg.decodeMessage();

        // the openhab plugin is not (yet) using the cmndseqnbr & seqnbr2
        String acceptedHexMessage = "0B150005D950450101000080";

        assertEquals("Message converted back", acceptedHexMessage, DatatypeConverter.printHexBinary(decoded));
    }
}