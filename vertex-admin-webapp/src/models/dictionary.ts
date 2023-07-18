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

  const getDictionary = (entryKey: string) => {
    entryKey = entryKey.toUpperCase();
    if (!dictionary) {
      return [];
    }
    return dictionary[entryKey] || [];
  }

  const getOptions = (entryKey: string) => {
    entryKey = entryKey
      .replace(/([A-Z])/g, "_$1")
      .replace(/^_/, "")
      .toUpperCase();
    return getDictionary(entryKey).map((item) => {
      return {
        label: item.label,
        value: item.label
      }
    });
  }

  return {
    dictionary,
    getDictionary,
    getOptions
  }
}