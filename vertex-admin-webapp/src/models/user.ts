import {useEffect, useState} from 'react';
import service from '@/services/user';

const useUser = () => {
  const [user, setUser] = useState<User>();

  useEffect(() => {
    if (location.href.includes('/login')) return;

    const userinfo = JSON.parse(localStorage.getItem('userinfo') || '{}');
    if (userinfo.id) {
      service.getUserInfo(userinfo.id).then((res) => {
        setUser(res);
      });
    }
  }, []);

  return {
    user,
    setUser,
  };
};

export default useUser;
