package com.github.icecheesecat.kantaicraft.menu.expedition;

import com.github.icecheesecat.kantaicraft.expedition.ExpeditionDefinition;
import com.github.icecheesecat.kantaicraft.playerkantaidata.PlayerExpeditionData;

public enum ExpeditionState {
    NO_SELECTION,
    DATA_UNAVAILABLE,
    READY,
    IN_PROGRESS,
    DONE;

    private static final String NO_SELECTION_KEY = "expedition_screen.status.no_selection";
    private static final String DATA_UNAVAILABLE_KEY = "expedition_screen.status.unavailable";
    private static final String READY_KEY = "expedition_screen.status.ready";
    private static final String IN_PROGRESS_KEY = "expedition_screen.status.in_progress";
    private static final String DONE_KEY = "expedition_screen.status.done";

    public String getTranslationKey() {
        return switch (this) {
            case NO_SELECTION -> NO_SELECTION_KEY;
            case DATA_UNAVAILABLE -> DATA_UNAVAILABLE_KEY;
            case READY -> READY_KEY;
            case IN_PROGRESS -> IN_PROGRESS_KEY;
            case DONE -> DONE_KEY;
        };
    }

    public static ExpeditionState resolve(ExpeditionDefinition p_definition, PlayerExpeditionData p_data) {
        if (p_definition == null) return NO_SELECTION;
        if (p_data == null) return DATA_UNAVAILABLE;

        return p_data.findAssignment(p_definition.getId())
                // Match the server boundary without consulting its definition manager on the client.
                .map(p_assignment -> p_assignment.getProgress() > p_definition.getDurationTicks()
                        ? DONE : IN_PROGRESS)
                .orElse(READY);
    }
}
