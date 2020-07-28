export const createModules = (moduleName, defines = {}) => {
  const modules = {}
  Object.keys(defines).forEach(name => {
    name = name.toUpperCase()
    const children = defines[name]
    if (children && children.length) {
      if (name === 'M') {
        children.forEach(defName => {
          modules[defName] = `${moduleName}:${defName.toUpperCase()}`
        })
      } else {
        modules[name] = {}
        children.forEach(defName => {
          modules[name][defName] = `${moduleName}:${name}:${defName.toUpperCase()}`
        })
      }
    }
  })
  return modules
}
