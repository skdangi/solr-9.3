/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.client.solrj.routing;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ShufflingReplicaListTransformer implements ReplicaListTransformer {

  /** Unique seed per call to avoid same-nanosecond collisions (correlated shuffle order). */
  private static final AtomicLong seedCounter = new AtomicLong();

  @SuppressWarnings("unused")
  public ShufflingReplicaListTransformer(Random r) {
    // r not used; transform() uses per-call Random to avoid contention.
  }

  @Override
  public <T> void transform(List<T> choices) {
    if (choices.size() > 1) {
      // Per-call Random with unique seed: no contention, no seed collision in tight loops.
      Collections.shuffle(choices, new Random(seedCounter.getAndIncrement()));
    }
  }
}
