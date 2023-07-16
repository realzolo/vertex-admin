// 子页Props
interface SubPageProps {
  visible: boolean;
  hide: (refresh?: boolean) => void;
  itemKey: string | number | undefined;
  data?: unknown;
}

// 模态框Props
interface ModalProps extends SubPageProps {
}

// 路由
declare interface Route {
  id: number;
  parentId: number;
  name: string;
  path: string;
  element?: ReactNode;
  children: Route[],
}