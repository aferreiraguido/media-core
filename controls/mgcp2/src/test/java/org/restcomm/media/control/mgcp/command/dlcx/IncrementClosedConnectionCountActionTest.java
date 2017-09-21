/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.media.control.mgcp.command.dlcx;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.restcomm.media.control.mgcp.command.MgcpCommandParameters;
import org.restcomm.media.control.mgcp.connection.MgcpConnection;
import org.restcomm.media.control.mgcp.endpoint.MgcpEndpointManager;

/**
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class IncrementClosedConnectionCountActionTest {

    @Test
    public void testClosedAllConnections() {
        // given
        final int transactionId = 12345;
        final MgcpCommandParameters parameters = new MgcpCommandParameters();
        final MgcpEndpointManager endpointManager = mock(MgcpEndpointManager.class);
        final DeleteConnectionContext context = spy(new DeleteConnectionContext(transactionId, parameters, endpointManager));
        final DeleteConnectionFsm fsm = mock(DeleteConnectionFsm.class);

        final MgcpConnection connection1 = mock(MgcpConnection.class);
        final MgcpConnection connection2 = mock(MgcpConnection.class);
        final MgcpConnection connection3 = mock(MgcpConnection.class);
        final MgcpConnection[] unregistered = new MgcpConnection[] { connection1, connection2, connection3 };

        context.setUnregisteredConnections(unregistered);

        // when
        IncrementClosedConnectionCountAction action = new IncrementClosedConnectionCountAction();
        action.execute(DeleteConnectionState.UNREGISTERING_CONNECTIONS, DeleteConnectionState.CLOSING_CONNECTIONS, DeleteConnectionEvent.UNREGISTERED_CONNECTIONS, context, fsm);
        action.execute(DeleteConnectionState.UNREGISTERING_CONNECTIONS, DeleteConnectionState.CLOSING_CONNECTIONS, DeleteConnectionEvent.UNREGISTERED_CONNECTIONS, context, fsm);
        action.execute(DeleteConnectionState.UNREGISTERING_CONNECTIONS, DeleteConnectionState.CLOSING_CONNECTIONS, DeleteConnectionEvent.UNREGISTERED_CONNECTIONS, context, fsm);

        // then
        verify(context, times(3)).incrementAndGetConnectionsClosed();
        verify(fsm).fire(DeleteConnectionEvent.CLOSED_CONNECTIONS, context);
    }

}
