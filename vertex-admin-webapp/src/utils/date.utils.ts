/**
 * 计算给定时间与当前时间的时间差，返回一个可读的时间描述
 * @param time 时间
 */
export const getTimeAgo = (time: string | number | Date) => {
  if (!time) return '';
  const targetTime = typeof time === 'string' || typeof time === 'number' ? new Date(time) : time;
  const currentTime = new Date();

  let timeDiff = currentTime.getTime() - targetTime.getTime();
  let seconds = Math.floor(timeDiff / 1000);
  let minutes = Math.floor(seconds / 60);
  let hours = Math.floor(minutes / 60);
  let days = Math.floor(hours / 24);

  if (days > 0) {
    return days + '天前';
  } else if (hours > 0) {
    return hours + '小时前';
  } else if (minutes > 0) {
    return minutes + '分钟前';
  } else {
    return '刚刚';
  }
}