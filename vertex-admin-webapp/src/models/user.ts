import {useEffect, useState} from 'react';
import {isLoginPage} from "@/utils/security.utils";

const useUser = () => {
  const [user, setUser] = useState<User>();

  useEffect(() => {
    if (isLoginPage()) return;

  }, []);

  return {
    user,
    setUser,
  };
};

export default useUser;
