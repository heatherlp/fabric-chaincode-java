/*
Copyright IBM Corp. All Rights Reserved.

SPDX-License-Identifier: Apache-2.0
*/

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.fvt.ChaincodeFVTest;
import org.hyperledger.fabric.shim.mock.peer.ChaincodeMockPeer;
import org.hyperledger.fabric.shim.mock.peer.RegisterStep;
import org.hyperledger.fabric.shim.mock.peer.ScenarioStep;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.READY;
import static org.hyperledger.fabric.protos.peer.ChaincodeShim.ChaincodeMessage.Type.REGISTER;
import static org.junit.Assert.assertThat;

public class ChaincodeWithoutPackageTest {
    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    ChaincodeMockPeer server;

    @After
    public void afterTest() throws Exception {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    public void testRegisterChaincodeWithoutPackage() throws Exception {
        ChaincodeBase cb = new EmptyChaincodeWithoutPackage();

        List<ScenarioStep> scenario = new ArrayList<>();
        scenario.add(new RegisterStep());

        server = ChaincodeMockPeer.startServer(scenario);

        cb.start(new String[]{"-a", "127.0.0.1:7052", "-i", "testId"});

        ChaincodeMockPeer.checkScenarioStepEnded(server, 1, 5000, TimeUnit.MILLISECONDS);

        assertThat(server.getLastMessageSend().getType(), is(READY));
        assertThat(server.getLastMessageRcvd().getType(), is(REGISTER));
    }

}
