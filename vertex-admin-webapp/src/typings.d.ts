// 子页Props
interface SubPageProps {
  visible: boolean;
  hide: (flush?: boolean) => void;
  itemKey: string | number | undefined;
  data?: unknown;
}

// 模态框Props
interface ModalProps extends SubPageProps {
}

declare interface Route {
  id: string;
  parentId: string;
  name: string;
  path: string;
  element?: ReactNode;
  children: Route[],
}