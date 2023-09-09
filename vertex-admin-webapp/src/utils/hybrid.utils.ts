/**
 * 删除对象中的空属性
 * @param obj
 */
export const removeEmpty = (obj: Record<string, any>) => {
  Object.keys(obj).forEach(key => {
    if (obj[key] === undefined || obj[key] === null || obj[key] === '') {
      delete obj[key];
    }
  });
  return obj;
}