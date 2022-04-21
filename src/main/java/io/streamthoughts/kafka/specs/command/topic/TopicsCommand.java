/*
 * Copyright 2021 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.kafka.specs.command.topic;

import io.streamthoughts.kafka.specs.change.ChangeResult;
import io.streamthoughts.kafka.specs.change.TopicChange;
import io.streamthoughts.kafka.specs.change.TopicChangeOptions;
import io.streamthoughts.kafka.specs.command.WithSpecificationCommand;
import io.streamthoughts.kafka.specs.command.topic.subcommands.Alter;
import io.streamthoughts.kafka.specs.command.topic.subcommands.Apply;
import io.streamthoughts.kafka.specs.command.topic.subcommands.Create;
import io.streamthoughts.kafka.specs.command.topic.subcommands.Delete;
import io.streamthoughts.kafka.specs.command.topic.subcommands.Describe;
import io.streamthoughts.kafka.specs.config.JikkouConfig;
import io.streamthoughts.kafka.specs.manager.KafkaResourceManager;
import io.streamthoughts.kafka.specs.manager.KafkaResourceUpdateContext;
import io.streamthoughts.kafka.specs.manager.KafkaTopicManager;
import io.streamthoughts.kafka.specs.manager.adminclient.AdminClientKafkaTopicManager;
import io.streamthoughts.kafka.specs.model.V1SpecObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.Collection;
import java.util.List;

@Command(name = "topics",
        headerHeading = "Usage:%n%n",
        descriptionHeading = "%nDescription:%n%n",
        parameterListHeading = "%nParameters:%n%n",
        optionListHeading = "%nOptions:%n%n",
        commandListHeading = "%nCommands:%n%n",
        synopsisHeading = "%n",
        header = "Apply the Topic changes described by your specs-file against the Kafka cluster you are currently pointing at.",
        description = "This command can be used to create, alter, delete or describe Topics on a remote Kafka cluster",
        subcommands = {
                Alter.class,
                Apply.class,
                Create.class,
                Delete.class,
                Describe.class,
                CommandLine.HelpCommand.class
        },
        mixinStandardHelpOptions = true)
public class TopicsCommand {

    public static abstract class Base extends WithSpecificationCommand<TopicChange> {

        public abstract KafkaResourceManager.UpdateMode getUpdateMode();

        public abstract TopicChangeOptions getChangeOptions();

        /**
         * {@inheritDoc}
         */
        @Override
        public Collection<ChangeResult<TopicChange>> execute(List<V1SpecObject> objects) {
            final KafkaTopicManager manager = new AdminClientKafkaTopicManager(JikkouConfig.get());

            return manager.update(
                    getUpdateMode(),
                    objects,
                    KafkaResourceUpdateContext.with(
                            Base.this::isResourceCandidate,
                            getChangeOptions(),
                            isDryRun()
                    )
            );
        }
    }
}
