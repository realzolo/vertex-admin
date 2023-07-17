import {useEffect, useState} from "react";
import service from "@/services/dictionary";
import {isLoginPage} from "@/utils/route.utils";

interface DictEntryMap {
  [key: string]: SelectOption[];
}

export default () => {
  const [dictionary, setDictionary] = useState<DictEntryMap>({});

  useEffect(() => {
    if (isLoginPage()) return;

    service.getDictionary().then((res) => {
      setDictionary(res);
    });
  }, []);

  /** 获取字典项 */
  const getEntry = (entryKey: string) => {
    entryKey = toUpperUnderscore(entryKey);
    if (!dictionary) {
      return [];
    }
    return dictionary[entryKey] || [];
  }

  /** @deprecated */
  const getOptions = (entryKey: string) => {
    console.warn("getOptions 已经废弃，请使用 buildOptions");
    entryKey = toUpperUnderscore(entryKey);
    return getEntry(entryKey).map((item) => {
      return {
        label: item.label,
        value: item.label
      }
    });
  }

  /** 字符串转大写下划线格式 */
  const toUpperUnderscore = (str: string) => {
    return str
      .replace(/([A-Z])/g, "_$1")
      .replace(/^_/, "")
      .toUpperCase();
  }

  /** 构建选项 */
  const buildOptions = (entryKey: string, ...ignoreFields: string[]): OptionType[] => {
    ignoreFields = ignoreFields.map((item) => toUpperUnderscore(item));
    const options = getEntry(entryKey).map((item) => {
      if (!ignoreFields.includes(item.label)) {
        return {
          label: item.label,
          value: item.value
        } as OptionType;
      }
    });
    return options.filter((item) => !!item) as OptionType[];
  }

  /**
   * 根据entryKey和value获取label
   * @param entryKey
   * @param value
   */
  const getLabel = (entryKey: string, value: number): string => {
    const entry = getEntry(entryKey).find((item) => item.value === value);
    return entry ? entry.label : "";
  }

  return {
    dictionary,
    getEntry,
    getOptions,
    buildOptions,
    getLabel
  }
}