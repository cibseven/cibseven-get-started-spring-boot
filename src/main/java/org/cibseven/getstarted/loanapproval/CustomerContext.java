package org.cibseven.getstarted.loanapproval;

import io.vavr.control.Option;

public class CustomerContext {
    public Customer customer;
    public Preferences preferences;

    public static class Customer {
        public String name;
        public Option<Integer> age; // Vavr Option
    }

    public static class Preferences {
        public String language;
        public String timezone;
    }
}
