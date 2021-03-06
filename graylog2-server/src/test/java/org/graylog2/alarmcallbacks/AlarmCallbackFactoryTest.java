package org.graylog2.alarmcallbacks;

import junit.framework.Assert;
import org.graylog2.plugin.alarms.callbacks.AlarmCallback;
import org.graylog2.shared.bindings.InstantiationService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Dennis Oelkers <dennis@torch.sh>
 */
public class AlarmCallbackFactoryTest {
    private AlarmCallbackFactory alarmCallbackFactory;
    private InstantiationService instantiationService;
    private DummyAlarmCallback dummyAlarmCallback;

    public interface DummyAlarmCallback extends AlarmCallback {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        this.instantiationService = mock(InstantiationService.class);
        this.dummyAlarmCallback = mock(DummyAlarmCallback.class);
        when(instantiationService.getInstance(DummyAlarmCallback.class)).thenReturn(dummyAlarmCallback);

        this.alarmCallbackFactory = new AlarmCallbackFactory(instantiationService);
    }

    @Test
    public void testCreateByAlarmCallbackConfiguration() throws Exception {
        AlarmCallbackConfiguration configuration = mock(AlarmCallbackConfiguration.class);
        when(configuration.getType()).thenReturn(DummyAlarmCallback.class.getName());

        AlarmCallback alarmCallback = alarmCallbackFactory.create(configuration);

        Assert.assertTrue(alarmCallback instanceof DummyAlarmCallback);
        Assert.assertEquals(dummyAlarmCallback, alarmCallback);
    }

    @Test
    public void testCreateByClassName() throws Exception {
        String className = DummyAlarmCallback.class.getName();

        AlarmCallback alarmCallback = alarmCallbackFactory.create(className);

        Assert.assertTrue(alarmCallback instanceof DummyAlarmCallback);
        Assert.assertEquals(dummyAlarmCallback, alarmCallback);
    }

    @Test
    public void testCreateByClass() throws Exception {
        AlarmCallback alarmCallback = alarmCallbackFactory.create(DummyAlarmCallback.class);

        Assert.assertTrue(alarmCallback instanceof DummyAlarmCallback);
        Assert.assertEquals(dummyAlarmCallback, alarmCallback);
    }
}
