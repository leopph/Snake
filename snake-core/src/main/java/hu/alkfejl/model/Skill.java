package hu.alkfejl.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;


public class Skill implements Serializable
{
    /* PROPERTIES */
    private final ObjectProperty<Instant> m_LastUsed;
    private final ObjectProperty<Duration> m_Cooldown;
    private final ObjectProperty<Duration> m_Duration;
    private final LongProperty m_Ticks;
    /* THESE ARE REDUNDANT BUT MAKE THE INTERFACE NICER */
    private final ReadOnlyBooleanWrapper m_InUse;
    private final ReadOnlyBooleanWrapper m_OnCooldown;


    /* CONSTRUCTORS */
    public Skill()
    {
        m_LastUsed = new SimpleObjectProperty<>(Instant.MIN);
        m_Cooldown = new SimpleObjectProperty<>(Duration.ZERO);
        m_Duration = new SimpleObjectProperty<>(Duration.ZERO);
        m_Ticks = new SimpleLongProperty(0);
        m_InUse = new ReadOnlyBooleanWrapper(false);
        m_OnCooldown = new ReadOnlyBooleanWrapper(false);

        m_InUse.bind(Bindings.createBooleanBinding(() -> Instant.now().isBefore(m_LastUsed.get().plus(m_Duration.get())), m_LastUsed, m_Duration, m_Ticks));
        m_OnCooldown.bind(Bindings.createBooleanBinding(() -> Instant.now().isBefore(m_LastUsed.get().plus(m_Cooldown.get())), m_LastUsed, m_Cooldown, m_Ticks));
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ObjectProperty<Instant> lastUsedProperty() { return m_LastUsed; }
    public ObjectProperty<Duration> cooldownProperty() { return m_Cooldown; }
    public ObjectProperty<Duration> durationProperty() { return m_Duration; }
    public LongProperty ticksProperty() { return m_Ticks; }
    public ReadOnlyBooleanProperty inUseProperty() { return m_InUse.getReadOnlyProperty(); }
    public ReadOnlyBooleanProperty onCooldownProperty() { return m_OnCooldown.getReadOnlyProperty(); }

    public Instant getLastUsed() { return m_LastUsed.get(); }
    public Duration getCooldown() { return m_Cooldown.get(); }
    public Duration getDuration() { return m_Duration.get(); }
    public Long getTicks() { return m_Ticks.get(); }
    public Boolean isInUse() { return m_InUse.get(); }
    public Boolean isOnCooldown() { return m_OnCooldown.get(); }

    public void setLastUsed(Instant newValue) { m_LastUsed.set(newValue); }
    public void setCooldown(Duration newValue) { m_Cooldown.set(newValue); }
    public void setDuration(Duration newValue) { m_Duration.set(newValue); }
    public void setTicks(Long newValue) { m_Ticks.set(newValue); }
}
