import {request} from "@@/plugin-request";
import {buildRoutes} from "@/utils/route.utils";

export const getRoutes = async (userId: number) => {
  const res = await request<any>(`/api/menu/user-menu`, {
    method: 'GET',
    params: {
      userId
    }
  });
  return buildRoutes(res.data, 0);
};
