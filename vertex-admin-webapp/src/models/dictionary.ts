import {useEffect, useState} from "react";
import service from "@/services/dictionary";
import {isLoginPage} from "@/utils/security.utils";

interface DictionaryMap {
  [key: string]: SelectOption[];
}

export default () => {
  const [dictionary, setDictionary] = useState<DictionaryMap>({});

  useEffect(() => {
    if (isLoginPage()) return;

    service.fetchDictionary().then((res) => {
      setDictionary(res);
    });
  }, []);

  /**
   * 获取字典项
   * @param dictKey 字典key
   */
  const getItems = (dictKey: string) => {
    dictKey = toLowerUnderscore(dictKey);
    if (!dictionary) {
      return [];
    }
    return dictionary[dictKey] || [];
  }

  /**
   * 字符串转小写下划线
   * @param str
   */
  const toLowerUnderscore = (str: string) => {
    return str
      .replace(/([A-Z])/g, "_$1")
      .replace(/^_/, "")
      .toLowerCase();
  }

  /**
   * 获取选项
   * @param dictKey 字典key
   * @param ignoreFields 忽略的字段
   * @returns
   */
  const getOptions = (dictKey: string, ...ignoreFields: string[]): SelectOption[] => {
    ignoreFields = ignoreFields.map((field) => toLowerUnderscore(field));
    const options = getItems(dictKey).map((item) => {
      if (!ignoreFields.includes(item.label)) {
        return {
          label: item.label,
          value: item.value
        } as SelectOption;
      }
    });
    return options.filter((item) => !!item) as SelectOption[];
  }

  /**
   * 根据dictKey和value获取label
   * @param dictKey
   * @param value
   */
  const getLabel = (dictKey: string, value: number): string => {
    const entry = getItems(dictKey).find((item) => item.value === value);
    return entry ? entry.label : "";
  }

  /**
   * 根据dictKey和label获取value
   * @param dictKey
   * @param label
   */
  const getValue = (dictKey: string, label: string): number => {
    const entry = getItems(dictKey).find((item) => item.label === label);
    return entry ? entry.value : -1;
  }

  return {
    dictionary,
    getItems,
    getOptions,
    getLabel,
    getValue
  }
}