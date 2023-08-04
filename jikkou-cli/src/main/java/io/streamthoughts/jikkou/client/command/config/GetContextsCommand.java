/*
 * Copyright 2023 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.jikkou.client.command.config;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import io.streamthoughts.jikkou.client.context.ConfigurationContext;
import io.streamthoughts.jikkou.client.context.Context;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Map;
import picocli.CommandLine.Command;

@Command(
        name = "get-contexts",
        description = "Get all contexts"
)
@Singleton
public class GetContextsCommand implements Runnable {

    @Inject
    private ConfigurationContext configurationContext;

    @Override
    public void run() {
        Map<String, Context> contexts = configurationContext.getContexts();
        String current = configurationContext.getCurrentContextName();

        String[][] data = contexts.keySet()
                .stream()
                .map(context -> new String[]{context + (context.equals(current) ? "*" : "")})
                .toArray(String[][]::new);

        String table = AsciiTable.getTable(AsciiTable.NO_BORDERS,
                new Column[]{
                        new Column().header("NAME").dataAlign(HorizontalAlign.LEFT),
                },
                data);

        System.out.println(table);
    }
}