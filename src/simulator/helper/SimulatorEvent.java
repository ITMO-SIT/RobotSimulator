package simulator.helper;


public enum SimulatorEvent {
    SIMULATION_INIT,
    SIMULATION_WAIT,
    SIMULATION_STEP_END,
    SIMULATION_END,
    SIMULATION_PAUSE,
    SIMULATION_START,


    WRAPPER_PAUSE {
        @Override
        public String toString() {
            return "Приостановлен";
        }
    },
    WRAPPER_WORK {
        @Override
        public String toString() {
            return "Работает";
        }
    },
    WRAPPER_END {
        @Override
        public String toString() {
            return "Завершено";
        }
    };


    static public boolean isWrapperEvent(SimulatorEvent event) {
        return event == WRAPPER_END || event == WRAPPER_PAUSE || event == WRAPPER_WORK;
    }
}
