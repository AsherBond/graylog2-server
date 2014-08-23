package org.graylog2.outputs;

import com.beust.jcommander.internal.Lists;
import com.codahale.metrics.*;
<<<<<<< HEAD
import org.graylog2.Core;
import org.graylog2.plugin.GraylogServer;
=======
import org.graylog2.Configuration;
import org.graylog2.indexer.Indexer;
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
import org.graylog2.plugin.Message;
import org.graylog2.plugin.outputs.OutputStreamConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
<<<<<<< HEAD
=======
import java.util.concurrent.ThreadPoolExecutor;
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e

import static com.codahale.metrics.MetricRegistry.name;

/**
 * @author Dennis Oelkers <dennis@torch.sh>
 */
public class BatchedElasticSearchOutput extends ElasticSearchOutput {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final List<Message> buffer;
    private final int maxBufferSize;
    private final ExecutorService flushThread;
    private final Timer processTime;
    private final Histogram batchSize;
    private final Meter bufferFlushes;
    private final Meter bufferFlushesRequested;
<<<<<<< HEAD
    private final Core core;

    @Inject
    public BatchedElasticSearchOutput(Core core) {
        super(core);
        this.core = core;
        this.buffer = Lists.newArrayList();
        this.maxBufferSize = core.getConfiguration().getOutputBatchSize();
        this.flushThread = Executors.newSingleThreadExecutor();
        MetricRegistry metricRegistry = core.metrics();
=======

    @Inject
    public BatchedElasticSearchOutput(MetricRegistry metricRegistry, Indexer indexer, Configuration configuration) {
        super(metricRegistry, indexer);
        this.buffer = Lists.newArrayList();
        this.maxBufferSize = configuration.getOutputBatchSize();
        this.flushThread = Executors.newSingleThreadExecutor();
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
        this.processTime = metricRegistry.timer(name(BatchedElasticSearchOutput.class, "processTime"));
        this.batchSize = metricRegistry.histogram(name(this.getClass(), "batchSize"));
        this.bufferFlushes = metricRegistry.meter(name(this.getClass(), "bufferFlushes"));
        this.bufferFlushesRequested = metricRegistry.meter(name(this.getClass(), "bufferFlushesRequested"));
    }

    @Override
<<<<<<< HEAD
    public void write(List<Message> messages, OutputStreamConfiguration streamConfig, GraylogServer core) throws Exception {
=======
    public void write(List<Message> messages, OutputStreamConfiguration streamConfig) throws Exception {
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
        synchronized (this.buffer) {
            this.buffer.addAll(messages);
            if (this.buffer.size() >= maxBufferSize) {
                flush();
            }
        }
    }

    public void synchronousFlush(List<Message> mybuffer) {
        LOG.debug("[{}] Starting flushing {} messages", Thread.currentThread(), mybuffer.size());

        try(Timer.Context context = this.processTime.time()) {
<<<<<<< HEAD
            super.write(mybuffer, null, core);
=======
            super.write(mybuffer, null);
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
            this.batchSize.update(mybuffer.size());
            this.bufferFlushes.mark();
        } catch (Exception e) {
            LOG.error("Unable to flush message buffer: {} - {}", e, e.getStackTrace());
        }
        LOG.debug("[{}] Flushing {} messages completed", Thread.currentThread(), mybuffer.size());
    }

    public void asynchronousFlush(final List<Message> mybuffer) {
        LOG.debug("Submitting new flush thread");
        flushThread.submit(new Runnable() {
            @Override
            public void run() {
                synchronousFlush(mybuffer);
            }
        });
    }

    public void flush() {
        this.bufferFlushesRequested.mark();
        List<Message> mybuffer;
        synchronized (this.buffer) {
            mybuffer = Lists.newArrayList(this.buffer);
            this.buffer.clear();
        }
        asynchronousFlush(mybuffer);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 84813ab619e8dba994e3cdc5b4eafd3ae75c908e
