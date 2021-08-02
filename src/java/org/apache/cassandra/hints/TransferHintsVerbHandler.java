/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.hints;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.net.IVerbHandler;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.service.StorageService;

/**
 * Verb handler used when another node asks for current node to transfer its hints
 */
public class TransferHintsVerbHandler implements IVerbHandler
{
    public static final TransferHintsVerbHandler instance = new TransferHintsVerbHandler();

    private static final Logger logger = LoggerFactory.getLogger(TransferHintsVerbHandler.class);

    public void doVerb(Message msg)
    {
        logger.debug("Received request from {} to transfer local to hints to closest neighbor.", msg.from());
        try
        {
            StorageService.instance.streamHintsBlocking();
        }
        catch (InterruptedException e)
        {
            return;
        }
        catch (ExecutionException e)
        {
            return;
        }
    }
}
