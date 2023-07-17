import service from '@/services/message';

const userinfo: User = JSON.parse(localStorage.getItem('userinfo') || '{}');

export const getUserinfo = () => {
  return userinfo;
}

export const getMessages = async () => {
  const userinfo = getUserinfo();
  if (!userinfo.id) return [];
  const messages = await service.getMessageList();
  return messages.items.sort((a, b) => b.id - a.id);
}
