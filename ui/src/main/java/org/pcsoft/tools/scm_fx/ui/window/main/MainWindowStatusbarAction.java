package org.pcsoft.tools.scm_fx.ui.window.main;

/**
 * Created by pfeifchr on 08.10.2014.
 */
interface MainWindowStatusbarAction {

    void showHint(String hint);

    void hideHint();

    void showProcess(String action);

    /**
     * Call first {@link org.pcsoft.tools.scm_fx.ui.window.main.MainWindowStatusbarController#showProcess(String)}
     *
     * @param action
     */
    void updateProcessText(String action);

    /**
     * Call first {@link org.pcsoft.tools.scm_fx.ui.window.main.MainWindowStatusbarController#showProcess(String)}
     *
     * @param progress
     */
    void updateProcessProgress(double progress);

    void hideProgress();

}
