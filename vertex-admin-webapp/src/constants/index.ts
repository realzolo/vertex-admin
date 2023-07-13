export const DEFAULT_NAME = 'Umi Max';
export const DEFAULT_PAGINATION = {
  page: 1,
  pageSize: 10,
};
export const DEFAULT_PRO_TABLE_PROPS = {
  pagination: DEFAULT_PAGINATION,
  rowKey: 'id',
}

export const DEFAULT_DRAWER_PROPS = {
  width: '85%',
  mask: true,
  destroyOnClose: true,
}

export const FILE_TYPE_MAP = {
  'image': '图片',
  'video': '视频',
  'audio': '音频',
  'document': '文档',
  'others': '其它',
}

/** 默认的最小加载时间 */
export const DEFAULT_MIN_LOADER_TIME = 500;

/** 是否选项枚举 */
export const YES_OR_NO_OPTIONS = {
  true: {text: '是', status: 'Success'},
  false: {text: '否', status: 'Error'},
}
