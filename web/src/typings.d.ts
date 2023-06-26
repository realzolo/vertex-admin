// 子页Props
interface SubPageProps {
  visible: boolean;
  hide: () => void;
  itemKey: string | number | undefined;
  data?: unknown;
}

// 模态框Props
interface ModalProps extends SubPageProps {
}