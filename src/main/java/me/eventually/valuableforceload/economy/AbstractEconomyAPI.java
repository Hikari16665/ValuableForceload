package me.eventually.valuableforceload.economy;

import me.eventually.valuableforceload.exceptions.NotSetupException;

/**
 * Abstract class for economy API
 * @author eventually
 * @param <T> Type parameter for the economy API
 * @implNote must implement {@link #setupApi()} and {@link #getApi()}
 */
public abstract class AbstractEconomyAPI<T> {
    /**
     * Set up the economy API
     * @return true if setup was successful
     */
    public abstract Boolean setupApi();
    /**
     * Get the economy API
     * @return {@link T} the economy API
     */
    public abstract T getApi() throws NotSetupException;
}
