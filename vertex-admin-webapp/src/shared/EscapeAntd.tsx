import {Fragment} from "react";
import {App, message as antdMessage, Modal as antdModal, notification as antdNotification,} from "antd";
import type {MessageInstance} from "antd/es/message/interface";
import type {NotificationInstance} from "antd/es/notification/interface";
import type {ModalStaticFunctions} from "antd/es/modal/confirm";

let message: MessageInstance = antdMessage;
let notification: NotificationInstance = antdNotification;

// because warn is deprecated, so we need to remove it.
const {warn, ...resetFns} = antdModal;
let modal: Omit<ModalStaticFunctions, "warn"> = resetFns;

/**
 * This component is used to escape the antd's static functions.
 */
const EscapeAntd = () => {
  const staticFunctions = App.useApp();

  message = staticFunctions.message;
  notification = staticFunctions.notification;
  modal = staticFunctions.modal;

  return <Fragment/>;
}

export {
  message,
  notification,
  modal,
}

export default EscapeAntd;
