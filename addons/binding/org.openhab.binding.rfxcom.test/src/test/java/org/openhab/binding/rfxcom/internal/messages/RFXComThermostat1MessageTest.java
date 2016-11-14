/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import static org.junit.Assert.assertEquals;
import static org.openhab.binding.rfxcom.internal.messages.RFXComThermostat1Message.Mode.COOLING;
import static org.openhab.binding.rfxcom.internal.messages.RFXComThermostat1Message.Mode.HEATING;
import static org.openhab.binding.rfxcom.internal.messages.RFXComThermostat1Message.Status.DEMAND;
import static org.openhab.binding.rfxcom.internal.messages.RFXComThermostat1Message.Status.NO_DEMAND;
import static org.openhab.binding.rfxcom.internal.messages.RFXComThermostat1Message.SubType.DIGIMAX;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComException;
import org.openhab.binding.rfxcom.internal.exceptions.RFXComNotImpException;

public class RFXComThermostat1MessageTest {
    private void testMessage(String hexMsg, RFXComThermostat1Message.SubType subType, int sequenceNumber, String sensorId, int temperature, int setpoint, RFXComThermostat1Message.Mode mode, RFXComThermostat1Message.Status status, int signalLevel) throws RFXComException, RFXComNotImpException {
        byte[] byteMessage = DatatypeConverter.parseHexBinary(hexMsg);
        RFXComThermostat1Message msg = (RFXComThermostat1Message) RFXComMessageFactory.createMessage(byteMessage);
        assertEquals("SubType", subType, msg.subType);
        assertEquals("Seq Number", sequenceNumber, (short) (msg.seqNbr & 0xFF));
        assertEquals("Sensor Id", sensorId, msg.getDeviceId());
        assertEquals("Temperature", temperature, msg.temperature);
        assertEquals("Set point", setpoint, msg.set);
        assertEquals("Mode", mode, msg.mode);
        assertEquals("Status", status, msg.status);
        assertEquals("Signal Level", signalLevel, msg.signalLevel);

        byte[] decoded = msg.decodeMessage();

        assertEquals("Message converted back", hexMsg, DatatypeConverter.printHexBinary(decoded));
    }

    @Test
    public void testSomeMessages() throws RFXComException, RFXComNotImpException {
        testMessage("0940001B6B1816150270", DIGIMAX, 27, "27416", 22, 21, HEATING, NO_DEMAND, 7);
        testMessage("0940001B6B1816158170", DIGIMAX, 27, "27416", 22, 21, COOLING, DEMAND, 7);
    }
}