import {defineConfig} from '@umijs/max';
import routes from "./routes";
import proxy from "./proxy";

const {NODE_ENV} = process.env;
const isDev: boolean = NODE_ENV === 'development';
const devConfig = {
  proxy,
  fastRefresh: true,
  monorepoRedirect: {},
  mfsu: true,
};
console.log(devConfig);
const proConfig = {};
export default defineConfig({
  antd: {},
  access: {},
  model: {},
  initialState: {},
  request: {},
  layout: {
    title: '@umijs/max',
  },
  routes: routes,
  npmClient: 'pnpm',
  // 更换打包路径：web/dist -> web/src/main/resources/static
  outputPath: '../src/main/resources/static',
  // 更换打包方式：webpack -> vite
  vite: {},
  hash: true,
  ...(isDev ? devConfig : proConfig),
});

